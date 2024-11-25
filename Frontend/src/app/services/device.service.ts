import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DeviceService {
  private baseUrl = environment.apiURL;

  constructor(private http: HttpClient) {}

  listDevices(): Observable<any> {
    return this.http.get(`${this.baseUrl}/list`);
  }

  listDevicesOfCompany(data: { name: string; password: string }): Observable<any> {
    return this.http.post(`/api/company/devices`, data);
  }
}
