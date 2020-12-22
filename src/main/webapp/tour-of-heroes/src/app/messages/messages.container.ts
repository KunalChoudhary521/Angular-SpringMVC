import { Component } from '@angular/core';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-messages',
  template: `<app-messages-ui [messages]="messages"
                              (clear)="clearMessages()"></app-messages-ui>`
})
export class MessagesContainerComponent {

  constructor(public messageService: MessageService) {}

  get messages() {
    return this.messageService.messages;
  }

  clearMessages() {
    this.messageService.clear();
  }

}
