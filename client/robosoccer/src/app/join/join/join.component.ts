import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionStorageService } from 'src/app/core/services/session-storage.service';
import { SocketService } from 'src/app/core/services/socket.service';

@Component({
  selector: 'app-join',
  templateUrl: './join.component.html',
  styleUrls: ['./join.component.css']
})
export class JoinComponent implements OnInit {

  username: FormControl = new FormControl('', [Validators.required]);
  errorMessage: string;

  constructor(
    private socketService: SocketService,
    private sessionStorageService: SessionStorageService,
    private router: Router) { }

  async ngOnInit () {
    if (this.socketService.connected) {
      return;
    }
  }

  async joinUser(): Promise<any> {
    this.errorMessage = '';
    try {
      const sessionId = await this.socketService.connect();
      const response = await this.socketService.joinUser(this.username.value, sessionId);
      this.sessionStorageService.createSession(response);
      this.router.navigateByUrl('/');
    } catch (err) {
      console.error(err);
      // TODO: unbox error message from err object
      this.errorMessage = err;
    }
  }

}
