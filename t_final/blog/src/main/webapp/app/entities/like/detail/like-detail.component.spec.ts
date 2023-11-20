import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { LikeDetailComponent } from './like-detail.component';

describe('Like Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LikeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LikeDetailComponent,
              resolve: { like: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LikeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load like on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LikeDetailComponent);

      // THEN
      expect(instance.like).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
