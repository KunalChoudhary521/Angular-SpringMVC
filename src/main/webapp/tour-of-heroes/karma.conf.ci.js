const baseConfig = require('./karma.conf.js');

module.exports = function(config){
  // Load base config
  baseConfig(config);

  // Override base config for CI build
  config.set({
    browsers: ['ChromeHeadless'],
    singleRun: true,
    autoWatch: false
  });
};
