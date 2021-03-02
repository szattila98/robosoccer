import { Injectable } from '@angular/core';
import { Session } from '../models/session';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService {

  private session: Session = null;

  createSession(session: Session): void {
    this.session = session;
  }

  deleteSession(): void {
    this.session = null;
  }

  getSession(): Session {
    return this.session;
  }
}
