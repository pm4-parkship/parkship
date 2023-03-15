import { withImageProxy } from '../../src/image-proxy';

export default withImageProxy({
  fallbackUrl: 'https://placehold.co/100x100.png'
});

export const config = {
  api: {
    responseLimit: false
  }
};
