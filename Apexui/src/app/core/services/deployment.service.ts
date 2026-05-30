import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface DeploymentClient {
  id: string;
  clientId: string;
  status: string;
  startedAt: string;
  finishedAt: string;
}

export interface Deployment {
  id: string;
  version: { id: number; versionNumber: string };
  deploymentType: string;
  status: string;
  notes: string;
  createdBy: string;
  createdAt: string;
  deploymentClients: DeploymentClient[];
}

@Injectable({ providedIn: 'root' })
export class DeploymentService {

  constructor(private http: HttpClient) {}

  createPilot(versionId: number, clientIds: string[]) {
    return this.http.post<Deployment>(
      `${environment.apiUrl}/deployments/pilot`,
      { versionId, clientIds }
    );
  }

  createFull(versionId: number) {
    return this.http.post<Deployment>(
      `${environment.apiUrl}/deployments/full`,
      { versionId }
    );
  }

  getById(id: string) {
    return this.http.get<Deployment>(`${environment.apiUrl}/deployments/${id}`);
  }

  getAll() {
    return this.http.get<Deployment[]>(`${environment.apiUrl}/deployments`);
  }
}
