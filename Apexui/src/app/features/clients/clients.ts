import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClientService } from '../../core/services/client.service';
import { Client } from '../../models/client.model';

// PrimeNG
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectButtonModule } from 'primeng/selectbutton';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import {Tooltip} from 'primeng/tooltip';

@Component({
  selector: 'app-clients',
  imports: [
    CommonModule, FormsModule,
    TableModule, ButtonModule, TagModule, CardModule,
    DialogModule, InputTextModule, SelectButtonModule,
    ToastModule, ConfirmDialogModule, Tooltip
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './clients.html',
  styleUrl: './clients.css',
})
export class Clients implements OnInit{
  clients: Client[] = [];
  loading = false;

  editDialogVisible = false;
  editName = '';
  editLocation = '';
  selectedClient: Client | null = null;

  keyDialogVisible = false;
  generatedKey = '';

  constructor(
    private clientService: ClientService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private cdr:ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadClients();
  }

  loadClients() {
    this.loading = true;
    this.clientService.getAll().subscribe({
      next: data => { this.clients = data; this.loading = false; this.cdr.detectChanges()},
      error: () => this.loading = false,
    });
  }

  sendCommand(client: Client, action: string) {
    this.clientService.sendCommand(client.id, action).subscribe({
      next: () => this.messageService.add({
        severity: 'success',
        summary: 'Success',
        detail: `${action} sent to ${client.name}`
      }),
      error: () => this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Failed to send command'
      })
    });
  }

  openEditDialog(client: Client) {
    this.selectedClient = client;
    this.editName = client.name;
    this.editLocation = client.location;
    this.editDialogVisible = true;
  }

  saveEdit() {
    if (!this.selectedClient) return;
    this.clientService.update(this.selectedClient.id, {
      name: this.editName,
      location: this.editLocation
    }).subscribe({
      next: (res) => {
        console.log(res)
        this.messageService.add({ severity: 'success', summary: 'Saved' });
        this.editDialogVisible = false;
        this.loadClients();
      }
    });
  }

  generateKey(client: Client) {
    this.clientService.generateInstallKey(client.id).subscribe({
      next: (res: any) => {
        this.generatedKey = res.key;
        this.keyDialogVisible = true;
      }
    });
  }

  copyKey() {
    navigator.clipboard.writeText(this.generatedKey);
    this.messageService.add({ severity: 'info', summary: 'Copied!' });
  }

  confirmDelete(client: Client) {
    this.confirmationService.confirm({
      message: `Delete ${client.name}?`,
      accept: () => {
        this.clientService.delete(client.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Deleted' });
            this.loadClients();
          }
        });
      }
    });
  }
}
