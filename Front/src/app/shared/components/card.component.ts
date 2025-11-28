import { Component, Input } from "@angular/core";
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div
      [ngStyle]="{ width: width, height: height }"
      class="bg-white rounded-[12px] p-4" style="box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);"
    >
      <ng-content></ng-content>
    </div>
  `,
})
export class CardComponent {
  @Input() width: string = '24rem';  
  @Input() height: string = 'auto';  
}