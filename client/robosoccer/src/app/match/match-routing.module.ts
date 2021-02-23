import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from './error/error.component';
import { FieldComponent } from './field/field.component';
import { MatchComponent } from './match.component';

const routes: Routes = [
    {
        path: '',
        component: MatchComponent,
        children: [
            {
                path: '',
                component: FieldComponent
            },
            {
                path: 'error',
                component: ErrorComponent
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MatchRoutingModule { }
