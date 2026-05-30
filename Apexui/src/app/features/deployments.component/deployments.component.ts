import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DeploymentService, Deployment } from '../../core/services/deployment.service';
import { VersionService } from '../../core/services/version.service';
import { ClientService } from '../../core/services/client.service';
import { Version } from '../../models/version.model';
import { Client } from '../../models/client.model';

// PrimeNG
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { DialogModule } from 'primeng/dialog';
import { Select } from 'primeng/select';
import { MultiSelectModule } from 'primeng/multiselect';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { StepsModule } from 'primeng/steps';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-deployments.component',
  imports: [
    CommonModule, FormsModule,
    CardModule, ButtonModule, TagModule,
    DialogModule, Select, MultiSelectModule,
    TableModule, ToastModule, StepsModule
  ],
  providers: [MessageService],
  templateUrl: './deployments.component.html',
  styleUrl: './deployments.component.css',
})
export class DeploymentsComponent implements OnInit {

  versions: Version[] = [];
  clients: Client[] = [];
  lastDeployment: Deployment | null = null;
  deployments: Deployment[] = [];

  pilotDialogVisible = false;
  fullDialogVisible = false;
  deploying = false;

  selectedVersionId: number | null = null;
  selectedClientIds: string[] = [];

  constructor(
    private deploymentService: DeploymentService,
    private versionService: VersionService,
    private clientService: ClientService,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.versionService.getAll().subscribe(data => this.versions = data);
    this.clientService.getAll().subscribe(data => this.clients = data);
    this.loadDeployments();
  }


  loadDeployments() {
    this.deploymentService.getAll().subscribe({
      next: data => this.deployments = data
    });
  }

  openPilotDialog() {
    this.selectedVersionId = null;
    this.selectedClientIds = [];
    this.pilotDialogVisible = true;
  }

  openFullDialog() {
    this.selectedVersionId = null;
    this.fullDialogVisible = true;
  }

  deployPilot() {
    if (!this.selectedVersionId) return;
    this.deploying = true;
    this.deploymentService.createPilot(this.selectedVersionId, this.selectedClientIds).subscribe({
      next: data => {
        this.lastDeployment = data;
        this.messageService.add({ severity: 'success', summary: 'Pilot deployment started' });
        this.pilotDialogVisible = false;
        this.deploying = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Deployment failed' });
        this.deploying = false;
      }
    });
  }

  deployFull() {
    if (!this.selectedVersionId) return;
    this.deploying = true;
    this.deploymentService.createFull(this.selectedVersionId).subscribe({
      next: data => {
        this.lastDeployment = data;
        this.messageService.add({ severity: 'success', summary: 'Full deployment started' });
        this.fullDialogVisible = false;
        this.deploying = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Deployment failed' });
        this.deploying = false;
      }
    });
  }

  statusSeverity(status: string) {
    switch (status) {
      case 'SUCCESS': case 'COMPLETED': return 'success';
      case 'FAILED': return 'danger';
      case 'IN_PROGRESS': case 'DOWNLOADING': return 'warn';
      default: return 'secondary';
    }
  }
}

