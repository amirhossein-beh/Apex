import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Client } from '../../models/client.model';

@Injectable({ providedIn: 'root' })
export class ClientService {

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Client[]>(`${environment.apiUrl}/clients`);
  }

  getById(id: string) {
    return this.http.get<Client>(`${environment.apiUrl}/clients/${id}`);
  }

  update(id: string, data: { name?: string; location?: string }) {
    return this.http.put<Client>(`${environment.apiUrl}/clients/${id}`, data);
  }

  delete(id: string) {
    return this.http.delete<string>(`${environment.apiUrl}/clients/${id}`);
  }

  generateInstallKey(id: string) {
    return this.http.post(`${environment.apiUrl}/clients/${id}/install-keys`, {});
  }

  sendCommand(id: string, action: string, payload: any = null) {
    return this.http.post(`${environment.apiUrl}/commands/${id}`, { action, payload });
  }
}
