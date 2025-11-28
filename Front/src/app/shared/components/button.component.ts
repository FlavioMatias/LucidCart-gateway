import { Component, Input, ElementRef, AfterViewInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <button
      [routerLink]="routerLink"
      class="flex items-center justify-center gap-3 rounded-lg border border-b-[2.5px] transition-all cursor-pointer duration-300 px-6 py-3"
      [style.width]="width"
      [style.height]="height"
      [style.borderColor]="filled ? color : '#0C1622'"
      [style.color]="filled ? 'white' : '#0C1622'"
      [style.backgroundColor]="filled ? color : 'transparent'"
      [ngStyle]="{
        '--hover-color': color
      }"
    >
      <i [innerHTML]="safeIcon"></i>
      <span class="font-medium">{{ text }}</span>
    </button>

  `,
  styles: ` 
    button:hover {
    background-color: var(--hover-color) !important;
    border-color: var(--hover-color) !important;
    color: white !important;
  }
`
})
export class ButtonComponent implements AfterViewInit {
  @Input() text: string = 'Botão';
  @Input() icon?: string;
  @Input() width: string = 'auto';
  @Input() height: string = '2.5rem';
  @Input() routerLink?: string | any[];
  @Input() filled: boolean = false;
  @Input() color: string = '#0C1622';

  safeIcon?: SafeHtml;

  constructor(private sanitizer: DomSanitizer, private el: ElementRef) {}

  ngAfterViewInit() {
    const button = this.el.nativeElement.querySelector('button');
    if (button) {
      button.addEventListener('mouseenter', () => {
        if (!this.filled) {
          button.style.backgroundColor = this.color;
          button.style.borderColor = this.color;
        }
      });
      button.addEventListener('mouseleave', () => {
        if (!this.filled) {
          button.style.backgroundColor = 'transparent';
          button.style.borderColor = '#111827';
        }
      });
    }
  }

  ngOnChanges() {
    if (this.icon) {
      this.safeIcon = this.sanitizer.bypassSecurityTrustHtml(this.icon);
    }
  }
}
