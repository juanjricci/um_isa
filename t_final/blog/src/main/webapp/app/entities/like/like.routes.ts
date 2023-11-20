import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LikeComponent } from './list/like.component';
import { LikeDetailComponent } from './detail/like-detail.component';
import { LikeUpdateComponent } from './update/like-update.component';
import LikeResolve from './route/like-routing-resolve.service';

const likeRoute: Routes = [
  {
    path: '',
    component: LikeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LikeDetailComponent,
    resolve: {
      like: LikeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LikeUpdateComponent,
    resolve: {
      like: LikeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LikeUpdateComponent,
    resolve: {
      like: LikeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default likeRoute;
