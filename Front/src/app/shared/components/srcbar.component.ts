import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

@Component({
  selector: 'app-srcbar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
<div class="flex justify-center">
  <div
    class="relative flex items-center border border-gray-600 rounded-[12px] overflow-hidden p-4"
    [ngStyle]="{ width: width, height: height }"
  >
    <input
      [(ngModel)]="value"
      [placeholder]="placeholder"
      (input)="onInputChange()"
      (focus)="onFocus()"
      (blur)="onBlur()"
      class="w-full pl-5 pr-14 text-gray-900 outline-none placeholder-gray-400"
    />

    <button
      (click)="onSearchClick()"
      class="absolute cursor-pointer right-0 top-0 h-full px-4 flex items-center justify-center bg-[#000000] hover:bg-[#182635] transition-colors"
    >
      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#fff" viewBox="0 0 256 256">
        <path d="M232.49,215.51,185,168a92.12,92.12,0,1,0-17,17l47.53,47.54a12,12,0,0,0,17-17ZM44,112a68,68,0,1,1,68,68A68.07,68.07,0,0,1,44,112Z"></path>
      </svg>
    </button>
  </div>
</div>
  `,
})
export class SrcBarComponent {
  @Input() placeholder: string = 'Buscar por algo...';
  @Input() width: string = '24rem';
  @Input() height: string = '2.75rem';

  @Output() valueChange = new EventEmitter<string>();
  @Output() focus = new EventEmitter<void>();  // ✅ novo
  @Output() blur = new EventEmitter<void>();   // ✅ novo

  value: string = '';

  onInputChange() {
    this.valueChange.emit(this.value);
  }

  onSearchClick() {
    this.valueChange.emit(this.value);
  }

  onFocus() {
    this.focus.emit();
  }

  onBlur() {
    this.blur.emit();
  }
}
