import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface TrafficLog {
  id: string;
  clientId: string;
  plateText: string;
  confidence: number;
  country: string;
  direction: string;
  streamId: number;
  plateImagePath: string;
  carImagePath: string;
  logDate: string;
  logTime: string;
  receivedAt: string;
  refId: String;
}

@Injectable({ providedIn: 'root' })
export class TrafficService {

  constructor(private http: HttpClient) {}

  getByClient(clientId: string) {
    return this.http.get<TrafficLog[]>(
      `${environment.apiUrl}/traffic/client/${clientId}`
    );
  }
}
