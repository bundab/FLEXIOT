import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private baseUrl = environment.apiURL + '/company';

  constructor(private http: HttpClient) {}

  registerCompany(data: { name: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }

  listCompanies(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/list`);
  }

  listUsers(data: { name: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/users`, data);
  }

  addPersonToCompany(username: string, data: { name: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/addPerson/${username}`, data);
  }

  createDeviceToCompany(data: { login: { name: string; password: string }; type: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/create_device`, data);
  }

  loginCompany(data: { name: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, data);
  }
}
