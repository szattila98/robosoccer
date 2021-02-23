import { Injectable } from '@angular/core';

const SESSION_STORAGE_KEY = 'userSession';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService {

  createSession(session: any): void {
    sessionStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(session));
  }

  deleteSession(): void {
    sessionStorage.removeItem(SESSION_STORAGE_KEY);
  }

  getSession(): any {
    const session = sessionStorage.getItem(SESSION_STORAGE_KEY);
    return JSON.parse(session);
  }
}
