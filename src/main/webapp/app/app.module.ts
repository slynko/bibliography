import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { ViewComponent } from './view.component';
import { EditComponent } from './edit.component';
import { MainComponent } from './main.component';
import { BibliographyService } from './service/bibliography.service';

@NgModule({
    imports: [
        BrowserModule,
        HttpModule,
        RouterModule.forRoot([
            {
                path: '',
                component: ViewComponent
            },
            {
                path: 'view',
                component: ViewComponent
            },
            {
                path: 'edit',
                component: EditComponent
            },
            {
                path: 'edit/:id',
                component: EditComponent
            },
            {
                path: '**',
                component: ViewComponent
            }
        ])
    ],
    declarations: [
        MainComponent,
        ViewComponent,
        EditComponent
    ],
    providers: [
        BibliographyService
    ],
    bootstrap: [MainComponent]
})
export class AppModule { }