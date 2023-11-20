import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'blog',
        data: { pageTitle: 'Blogs' },
        loadChildren: () => import('./blog/blog.routes'),
      },
      {
        path: 'post',
        data: { pageTitle: 'Posts' },
        loadChildren: () => import('./post/post.routes'),
      },
      {
        path: 'tag',
        data: { pageTitle: 'Tags' },
        loadChildren: () => import('./tag/tag.routes'),
      },
      {
        path: 'comment',
        data: { pageTitle: 'Comments' },
        loadChildren: () => import('./comment/comment.routes'),
      },
      {
        path: 'notification',
        data: { pageTitle: 'Notifications' },
        loadChildren: () => import('./notification/notification.routes'),
      },
      {
        path: 'like',
        data: { pageTitle: 'Likes' },
        loadChildren: () => import('./like/like.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
