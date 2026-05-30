import {Component, inject, WritableSignal} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

// PrimeNG
// import { SidebarModule } from 'primeng/';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { RippleModule } from 'primeng/ripple';
import {style} from '@angular/animations';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    ButtonModule,
    AvatarModule,
    RippleModule
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout {
  username = '';

  constructor(private authService: AuthService) {
    this.username = this.authService.getUsername();
  }

  logout() {
    this.authService.logout();
  }

  protected readonly style = style;
}
