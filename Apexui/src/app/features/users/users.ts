import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService, User } from '../../core/services/user.service';

// PrimeNG
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { Select } from 'primeng/select';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import {Tooltip} from 'primeng/tooltip';
@Component({
  selector: 'app-users',
  imports: [
    CommonModule, FormsModule,
    TableModule, CardModule, ButtonModule, TagModule,
    DialogModule, InputTextModule, PasswordModule,
    Select, ToastModule, ConfirmDialogModule, Tooltip
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './users.html',
  styleUrl: './users.css',
})
export class Users implements OnInit{
  users: User[] = [];
  loading = false;

  createDialogVisible = false;
  createForm = { username: '', password: '', role: '' };

  passwordDialogVisible = false;
  newPassword = '';
  selectedUser: User | null = null;

  roleOptions = [
    { label: 'Admin', value: 'ADMIN' },
    { label: 'Operator', value: 'OPERATOR' },
    { label: 'Viewer', value: 'VIEWER' }
  ];

  constructor(
    private userService: UserService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.userService.getAll().subscribe({
      next: data => { this.users = data; this.loading = false;this.cdr.detectChanges() },
      error: () => this.loading = false
    });
  }

  roleSeverity(role: string) {
    switch (role) {
      case 'ADMIN': return 'danger';
      case 'OPERATOR': return 'warn';
      default: return 'secondary';
    }
  }

  createUser() {
    this.userService.create(this.createForm).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'User created' });
        this.closeCreateDialog();
        this.loadUsers();
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: err.error ?? 'Failed to create user'
        });
      }
    });
  }

  closeCreateDialog() {
    this.createDialogVisible = false;
    this.createForm = { username: '', password: '', role: '' };
  }

  openPasswordDialog(user: User) {
    this.selectedUser = user;
    this.newPassword = '';
    this.passwordDialogVisible = true;
  }

  changePassword() {
    if (!this.selectedUser) return;
    this.userService.changePassword(this.selectedUser.id, this.newPassword).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Password changed' });
        this.passwordDialogVisible = false;
      }
    });
  }

  toggleActive(user: User) {
    this.userService.toggleActive(user.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Status updated' });
        this.loadUsers();
      }
    });
  }

  confirmDelete(user: User) {
    this.confirmationService.confirm({
      message: `Delete user ${user.username}?`,
      accept: () => {
        this.userService.delete(user.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'User deleted' });
            this.loadUsers();
          }
        });
      }
    });
  }

}
