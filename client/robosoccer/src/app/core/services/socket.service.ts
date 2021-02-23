import { Injectable } from '@angular/core';
import { CompatClient, Stomp } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SocketService {

  stompClient: CompatClient = null;

  constructor() {
    this.initializeConnection();
  }

  private initializeConnection(): void {
    if (this.stompClient !== null) {
      return;
    }

    this.stompClient = Stomp.over(() => new SockJS(environment.socketBaseUrl));
    this.stompClient.connect({}, (frame) => {
      this.stompClient.subscribe('/socket/game', (msg) => {
        console.log('MSG', msg);
        // TODO: join, receive data
        // this.stompClient.send('/api/join', {}, 'asd');
      });
    });

  }
}
