import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/guards/auth.guard';
import { ErrorComponent } from './error/error.component';
import { FieldComponent } from './field/field.component';
import { MatchComponent } from './match.component';

const routes: Routes = [
    {
        path: '',
        component: MatchComponent,
        canActivate: [AuthGuard],
        children: [
            {
                path: '',
                component: FieldComponent,
                canActivate: [AuthGuard]
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
