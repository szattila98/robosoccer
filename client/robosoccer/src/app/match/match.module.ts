import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatchComponent } from './match.component';
import { MatchRoutingModule } from './match-routing.module';
import { ErrorComponent } from './error/error.component';
import { FieldComponent } from './field/field.component';



@NgModule({
  declarations: [MatchComponent, ErrorComponent, FieldComponent],
  imports: [
    CommonModule,
    MatchRoutingModule
  ]
})
export class MatchModule { }
