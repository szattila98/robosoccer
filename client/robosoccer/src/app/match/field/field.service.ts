import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FieldService {

  constructor(private http: HttpClient) { }

  getMatchData(): Promise<any> {
    return this.http.get('assets/match.json').toPromise();
  }
}
