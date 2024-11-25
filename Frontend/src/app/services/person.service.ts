import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PersonService {
  private baseUrl = environment.apiURL;

  constructor(private http: HttpClient) {}

  registerPerson(data: { username: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }

  listPeople(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/usernames`);
  }

  listDevices(data: { name: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/devices`, data);
  }

  createDeviceToPerson(data: { login: { name: string; password: string }; type: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/create_device`, data);
  }

  loginPerson(data: { name: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, data);
  }
}
