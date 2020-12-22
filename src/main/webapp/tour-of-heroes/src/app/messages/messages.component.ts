import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-messages-ui',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent {
  @Input() messages;
  @Output() clear = new EventEmitter();

}
