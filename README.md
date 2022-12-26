# Embed Angular within Spring MVC

# Quick Start
1. Build an executable JAR: `mvn clean install`
   - For linux OS, change browser to `Chromium` in `karma.conf.js` and `ChromiumHeadless` in `karma.conf.ci.js`. Or use `--browsers` karma flag.
2. Run the JAR: `java -jar target/toh-0.0.1-SNAPSHOT.jar`
3. Browse to the UI: `http://localhost:9000/ui/`. Add, update and delete heroes in the **Heroes** tab.

# Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Angular Standalone Application](#angular-standalone-application)
3. [URL Routing](#url-routing)
4. [Project Build](#project-build)
5. [Application Packaging](#application-packaging)
6. [Calling API Endpoints](#calling-api-endpoints)
7. [Application Security](#application-security)
8. [Pros & Cons](#pros-and-cons)
9. [Tips & Tricks](#tips-and-tricks)

# Main Setup
## Architecture Overview
This is a simple 3-tier application with a UI, API and Database:
1. H2 database stores a `Hero` object in memory. 
2. API provides endpoints to perform simple CRUD operations on the `Hero` object. 
3. UI presents these `Heroes` in a Single-page application (SPA). 
   
***The goal of this project is to show how to serve an Angular application using Java Spring MVC.***

## Angular Standalone Application
Prior to integrating with Spring MVC, it is necessary to set up the Angular application and ensure that it runs independently. You can either create a brand-new Angular application or import an existing one. 

To create a new one, use `ng new <app-name>` (install Angular CLI globally using `npm install -g @angular/cli@latest`). I simply added an existing Angular application from [here](https://github.com/KunalChoudhary521/tour-of-heroes-and-villains). According to Maven standard directory [convention](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html), `src/main/webapp` directory is used to store web application sources. So, this is where I added the Angular application under the `tour-of-heroes` directory.

After the directory structure is created, ensure that the Angular application is running by executing `ng serve` and browsing to `http://localhost:4200`.

## URL routing
Since Angular application is embedded within Spring MVC project, the UI and API share the same host and port. In this project, Spring MVC serves the Angular’s production **"bundle"** instead of files from the `tour-of-heroes` development directory.

To create this bundle, run `ng build --prod` in the development directory. This command creates a `dist` folder which contains the HTML, CSS and JS files required to run in a production environment. The `index.html` file contains a custom HTML element named `<app-root></app-root>`, which is the root element and the entry-point for the Angular application. This project uses the Angular CLI to generate the production bundle named `ng-dist` folder under `src/main/webapp` with base href set to `/ui/`.

For the API, the REST endpoints have a Spring Controller at `/api/heroes` path. For the UI, I serve files from the `ng-dist` folder at the `/ui` path using the following configuration 
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {  

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/ui/**").addResourceLocations("classpath:/ng-dist/");
  }
}
```

Additionally, I added a Controller for the UI which forwards a request for `/ui` to `/ui/index.html`
```java
@Controller
@RequestMapping("/ui")
public class UIController { 

  @GetMapping()
  public String forwardToAngular() {
    return "forward:/ui/index.html";
  }
}
```

When you run the Spring Boot application through an IDE (ex. IntelliJ) and browse to `http://localhost:9000/ui/`, you will see the Angular application being served from the `/src/main/webapp/ng-dist` folder. This configuration is sufficient to route to the home page of the Angular application; however, visiting any routes in [app-routing.module.ts](https://github.com/KunalChoudhary521/Angular-SpringMVC/blob/master/src/main/webapp/tour-of-heroes/src/app/app-routing.module.ts) is not yet possible. When a request is made to a certain path (ex. `/ui/dashboard`), Spring MVC looks for a controller method for such a path and does not find one. As a result, the application returns **404 – not found**. Spring MVC needs to hand-off routing to the Angular for these UI routes. There are 2 ways to achieve this:
1. Define sub-paths in the `value` attribute of the @GetMapping annotation. This works if your UI has few routes, or if you are using Spring MVC 3 or lower.
```java
@Controller
@RequestMapping("/ui")
public class UIController {
  @GetMapping(value = { "/", "/dashboard", "/heroes", "/detail/**" })
   public String forwardToAngular() {
     return "forward:/ui/index.html";
   }
}
```
2. Replace `addResourceHandlers` method from above with the following one (credit to this [StackOverflow post](https://stackoverflow.com/a/46854105)). This tells Spring to route to Angular application’s root path if the Spring DispatcherServlet cannot find a certain path starting with `/ui`. **Note**: PathResourceResolver is available starting Spring MVC 4.1 ([documentation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/resource/PathResourceResolver.html)).
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/ui/**")
      .addResourceLocations("classpath:/ng-dist/")
      .resourceChain(true)
      .addResolver(new PathResourceResolver() {
           @Override
           protected Resource getResource(String resourcePath, Resource location) throws IOException {
               Resource requestedResource = location.createRelative(resourcePath);
               return requestedResource.exists() && requestedResource.isReadable() ?
                       requestedResource : new ClassPathResource("/ng-dist/index.html");
           }
       });
  }
}
```


## Project Build
As mentioned earlier, Spring MVC serves the Angular’s production "bundle" (or `ng-dist` folder) instead of the `tour-of-heroes` development directory. This bundle generation can be automated using the `frontend-maven-plugin` like so ([pom.xml](https://github.com/KunalChoudhary521/Angular-SpringMVC/blob/master/pom.xml)).

Instead of relying on the globally installed node and npm, this plugin downloads an OS-specific node and npm in the specified directory (in this case, `/src/main/webapp/tour-of-heroes`). Additionally, you can setup the plugin to run any npm script described in [package.json](https://github.com/KunalChoudhary521/Angular-SpringMVC/blob/master/src/main/webapp/tour-of-heroes/package.json). I used it to download dependencies (`npm install`), run unit tests (`npm run test-ci`), run E2E UI tests (`npm run e2e-ci`) and build the final “bundle” (`npm run build-ci`).

## Application Packaging
Since Spring MVC serves files from the Angular’s production "bundle", it is best to exclude the `tour-of-heroes` development directory from the `target/` directory and the JAR. To exclude this directory, add the following lines in the `<resources>...</resources>` section in [pom.xml](https://github.com/KunalChoudhary521/Angular-SpringMVC/blob/master/pom.xml#L166). Now, only including the `ng-dist/` directory is the final JAR; thus, keeping the artifact size small.

## Calling API Endpoints
To make a request to one of the Spring controller endpoints, simply create an Angular service, define a base URL and use Angular’s HTTP client ([like so](https://github.com/KunalChoudhary521/Angular-SpringMVC/blob/master/src/main/webapp/tour-of-heroes/src/app/hero.service.ts)). In this project, the Heroes Controller is defined at `/api/heroes`. **Note that calls from the UI to `api/heroes` is different from `/api/heroes`**. The former will append to the base-href of the Angular application (in this case, `/ui/`); whereas, the latter ignores it.

Generally, the UI and API are running as standalone application during development. In this case, the UI runs on `http://locahost:4200` and the API on `http://locahost:9000`. You can use a [proxy.config.js](https://github.com/KunalChoudhary521/Angular-SpringMVC/blob/master/src/main/webapp/tour-of-heroes/proxy.config.json) as a workaround this issue. The JSON object simply informs Angular (actually webpack) to call `http://locahost:9000/api/heroes` when a request is made to`http://locahost:4200/api/heroes`.

```json
{
    "/api/heroes": {
        "target": "http://localhost:9000",
        "secure": false,
        "logLevel": "debug",
        "changeOrigin": true
    }
}
```

# Application Security
To secure REST API endpoints, include the `spring-boot-starter-security` dependency and add Spring security configuration class [like so](https://github.com/KunalChoudhary521/Angular-SpringMVC/blob/master/src/main/java/com/backend/toh/config/WebSecurityConfig.java). Simply by including `@EnableWebSecurity`, Spring augments HTTP requests with the following headers:
 * Cache Control (Cache-Control: no-cache)
 * Content Type Options (x-content-type-options: nosniff)
 * X-Frame-Options (x-frame-options: DENY)
 * X-XSS-Protection (x-xss-protection: 1; mode=block)
These headers can be found in the `Network` tab in Chrome Developer tools. For more details about these headers, please refer to the [documentation](https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/headers.html). 

## Cross-Site Request Forgery (CSRF) protection
In addition to headers above, this project provides security against CSRF attacks. To enable, add the following configuration to Spring:
```java
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
```

Upon a GET request from the UI, Spring sends a CSRF token (`XSRF-TOKEN`) in a cookie. When the UI makes a state-changing request (ex. POST, PUT, DELETE), Angular places this token in the request header (`X-XSRF-TOKEN`). Any state-changing request with an invalid or missing CSRF token returns a Forbidden (`403`) status code. On the Angular side, set `withCredentials` http option to `true` in the service like so:
```js
const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    withCredentials: true
  };
```

# Pros and Cons
## Pros
1.  Angular application can be added to an existing project with minimal changes made to web and maven configuration.
2.  If the application currently supports UI from other technologies (ex. Ember, BackboneJS, KnockoutJS, jQuery, etc), an Angular component can be placed within that application. This can help gradually migrate from older frameworks to Angular.

## Cons
1.  The backend needs to at least use Spring MVC, as the setup for other technologies will be different.

# Tips and Tricks
## Maven
* Exclude a file/folder from the `target/` directory: [link](https://stackoverflow.com/a/25262893) 
* Include a file/folder to the `target/` directory: [link](https://stackoverflow.com/a/16375537)
* Skip re-installing node and npm with frontend-maven-plugin: [link](https://github.com/eirslett/frontend-maven-plugin/issues/768)

## Spring
* Serve static resources from custom location: [link](https://www.baeldung.com/spring-mvc-static-resources)
* Add a separate Spring controller for API & UI routing: [Spring MVC 3](https://stackoverflow.com/a/38778129) [Spring MVC 4+](https://stackoverflow.com/a/46854105)
* MIME type (red-herring) error: [link](https://stackoverflow.com/a/49701418). Spring boot static resource from JAR: [link](https://stackoverflow.com/a/39233671)
* Spring boot with H2 database: [link](https://www.baeldung.com/spring-boot-h2-database)
* Testing with H2 and DataJpaTest: [link](https://rieckpil.de/test-your-spring-boot-jpa-persistence-layer-with-datajpatest/)
* Testing controller methods: [link #1](https://www.baeldung.com/spring-boot-testing) [link #2](https://spring.io/guides/gs/testing-web/)
* Spring Security's Cross-site Request Forgery (CSRF): [link](https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/csrf.html)
* Testing controller methods with Spring Security enabled: [link](https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/test-mockmvc.html)

## Angular
* Ignore base-href (ex. /ui/) when making HTTP calls to api (ex: /api/heroes): [link](https://stackoverflow.com/a/62308890)
* Run Cypress E2E tests for CI build: [link](https://docs.cypress.io/guides/guides/continuous-integration.html#Boot-your-server)
