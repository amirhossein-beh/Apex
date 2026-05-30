import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VersionService } from '../../core/services/version.service';
import { Version } from '../../models/version.model';

// PrimeNG
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { FileUploadModule } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { TextareaModule } from 'primeng/textarea';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-versions',
  imports: [
    CommonModule, FormsModule,
    TableModule, ButtonModule, TagModule, CardModule,
    DialogModule, InputTextModule, CheckboxModule,
    FileUploadModule, ToastModule, TextareaModule
  ],
  providers: [MessageService],
  templateUrl: './versions.html',
  styleUrl: './versions.css',
})
export class Versions implements OnInit{
  versions: Version[] = [];
  loading = false;
  uploading = false;
  uploadDialogVisible = false;
  selectedFile: File | null = null;

  form = {
    versionNumber: '',
    minVersion: '',
    mandatory: false,
    releaseNotes: ''
  };

  constructor(
    private versionService: VersionService,
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadVersions();
  }

  loadVersions() {
    this.loading = true;
    this.versionService.getAll().subscribe({
      next: data => { this.versions = data; this.loading = false; this.cdr.detectChanges() ;
        console.log(data)},
      error: () => this.loading = false
    });
  }

  onFileSelect(event: any) {
    this.selectedFile = event.files[0];
  }

  upload() {
    if (!this.selectedFile || !this.form.versionNumber) return;

    const formData = new FormData();
    formData.append('versionNumber', this.form.versionNumber);
    formData.append('mandatory', String(this.form.mandatory));
    formData.append('minVersion', this.form.minVersion ?? '');
    formData.append('releaseNotes', this.form.releaseNotes ?? '');
    formData.append('file', this.selectedFile);

    this.uploading = true;
    this.versionService.upload(formData).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Uploaded successfully' });
        this.closeUploadDialog();
        this.loadVersions();
        this.uploading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Upload failed' });
        this.uploading = false;
      }
    });
  }

  activate(version: Version) {
    this.versionService.activate(version.id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: `Version ${version.versionNumber} activated`});
        this.loadVersions();
        this.cdr.detectChanges()
      }
    });
  }

  closeUploadDialog() {
    this.uploadDialogVisible = false;
    this.selectedFile = null;
    this.form = { versionNumber: '', minVersion: '', mandatory: false, releaseNotes: '' };
  }
}
