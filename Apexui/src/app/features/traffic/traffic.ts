import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TrafficService, TrafficLog } from '../../core/services/traffic.service';
import { ClientService } from '../../core/services/client.service';
import { Client } from '../../models/client.model';

// PrimeNG
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { Drawer } from 'primeng/drawer';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import {Select} from 'primeng/select';
@Component({
  selector: 'app-traffic',
  imports: [
    CommonModule, FormsModule,
    TableModule, CardModule, ButtonModule,
    TagModule, ToastModule, Select
  ],
  providers: [MessageService],
  templateUrl: './traffic.html',
  styleUrl: './traffic.css',
})
export class Traffic {
  logs: TrafficLog[] = [];
  clients: Client[] = [];
  selectedClientId: string = '';
  loading = false;

  constructor(
    private trafficService: TrafficService,
    private clientService: ClientService
  ) {}

  ngOnInit() {
    this.clientService.getAll().subscribe({
      next: data => this.clients = data
    });
  }

  loadTraffic() {
    if (!this.selectedClientId) return;
    this.loading = true;
    this.trafficService.getByClient(this.selectedClientId).subscribe({
      next: data => { this.logs = data; this.loading = false; },
      error: () => this.loading = false
    });
  }
}
