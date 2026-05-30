import {ChangeDetectorRef, Component, OnInit} from '@angular/core';

import { CommonModule } from '@angular/common';
import { ClientService } from '../../core/services/client.service';
import { Client } from '../../models/client.model';

// PrimeNG
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
@Component({
  selector: 'app-dashboard',
  imports: [
    CommonModule,
    CardModule,
    TagModule,
    TableModule,
    ButtonModule,
    BadgeModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit{
  clients: Client[] = [];
  loading = false;

  get onlineCount() {
    return this.clients.filter(c => c.status === 'ONLINE').length;
  }

  get offlineCount() {
    return this.clients.filter(c => c.status === 'OFFLINE').length;
  }

  constructor(private clientService: ClientService , private cdr:ChangeDetectorRef) {}

  ngOnInit() {
    this.loadClients();
  }

  loadClients() {
    this.loading = true;
    this.clientService.getAll().subscribe({
      next: data => {
        this.clients = data;
        this.loading = false;
        this.cdr.markForCheck()
      },
      error: () => {this.loading = false; this.cdr.markForCheck()}
    });
  }
}
