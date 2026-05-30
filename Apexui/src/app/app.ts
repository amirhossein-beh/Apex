import { Component, signal } from '@angular/core';
import {Button} from 'primeng/button';
import {RouterOutlet} from '@angular/router';
import {Layout} from './features/layout/layout';



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [Layout, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Apexui');
}
