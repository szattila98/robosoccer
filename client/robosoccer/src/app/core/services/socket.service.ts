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

  connect(): Promise<string> {
    return new Promise((resolve, reject) => {
      const socket = new SockJS(environment.socketBaseUrl);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {},
        // tslint:disable-next-line: no-string-literal
        frame => resolve(/(([a-z]|[0-9]){1,})(\/websocket)/.exec(socket['_transport'].url)[1]),
        err => reject(err),
        close => reject(new Error('Cannot connect to server'))
      );
    });
  }

  async joinUser(username: string, sessionId: string): Promise<any> {
    return new Promise((resolve, reject) => {
      if (!this.connected) {
        return reject(new Error('Can not connect to the server.'));
      }

      const joinSubscription = this.stompClient.subscribe('/socket/join', (message) => {
        let body;

        try {
          body = JSON.parse(message.body);
        } catch (err) {
          return reject(new Error('Server response can not be interpreted.'));
        }

        if (body && body.user && body.user.sessionId === sessionId) {
          joinSubscription.unsubscribe();
          resolve(body.user);
        }
      });

      this.stompClient.send('/api/join', {}, username);
    });
  }

}
