import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import { HttpClientModule, HttpClient } from '@angular/common/http';
import {HomeComponent} from "./components/home/home.component";
import {UserListComponent} from "./components/user-list/user-list.component";
import {CreateMachineComponent} from "./components/create-machine/create-machine.component";
import {ErrorMessageComponentComponent} from "./components/error-message-component/error-message-component.component";



const routes: Routes = [
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "",
    component: HomeComponent
  },
  {
    path: "list",
    component: UserListComponent
  },
  {
    path: "newMachine",
    component: CreateMachineComponent
  },
  {
    path: "errors",
    component: ErrorMessageComponentComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes),
    HttpClientModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
