import { Injectable } from '@angular/core';
import { CompatClient, Stomp } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SocketService {

  stompClient: CompatClient = null;

  constructor() { }

  get connected(): boolean {
    if (!this.stompClient) {
      return false;
    }

    return this.stompClient.connected;
  }

  async connect(): Promise<any> {
    const socket = new SockJS(environment.socketBaseUrl);
    this.stompClient = Stomp.over(socket);

    return new Promise((resolve, reject) => {
      this.stompClient.connect(
        {},
        frame => resolve(/(([a-z]|[0-9]){1,})(\/websocket)/.exec(socket['_transport'].url)[1]),
        err => reject(err)
      );
    });
  }

  async joinUser(username: string, sessionId: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.stompClient.subscribe('/socket/join', (message) => {
        const body = JSON.parse(message.body);
        if (body.user.sessionId === sessionId) {
          resolve(body.user);
        }
        // TODO: handle errors: JSON.parse, body object does not contain user field
      });
      this.stompClient.send('/api/join', {}, username);
    });
  }

}
