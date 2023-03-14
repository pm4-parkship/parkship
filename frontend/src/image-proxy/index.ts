import {NextApiRequest, NextApiResponse} from 'next';
import stream, {Stream} from 'stream';
import merge from 'lodash.merge';
import {DeepPartial, Options} from './types';
import {logger} from '../logger';

export function withImageProxy(passedOptions?: DeepPartial<Options>) {
  const defaultOptions: Options = {
    fallbackUrl: 'https://i.postimg.cc/HLWMztvH/amirlotan-the-jungle-book.gif',
    messages: {
      wrongFormat: 'Image url not provided or has wrong format',
      notWhitelisted: 'Provided image url is not whitelisted',
      imageFetchError: "Couldn't fetch the image"
    }
  };

  const options: Options = merge(defaultOptions, passedOptions);

  return async function (req: NextApiRequest, res: NextApiResponse) {
    let url = (await req.query.url) as string;

    if (
      !url ||
      url === '' ||
      url.match(
        /(http(s?):)([/|.|\w|\s|-])*\.(?:jpg|gif|png|webp|jpeg|bmp)/g
      ) === null
    ) {
      logger.warn(
        'Image url not provided or has wrong format going to fallback URL now',
        url
      );
      url = options.fallbackUrl;
    }

    const imageBlob = await fetchImageBlob(url);
    if (!imageBlob) {
      handleFallback(res, options);
      return;
    }

    pipeImage(res, imageBlob, options);
  };
}

function pipeImage(
  res: NextApiResponse,
  imageBlob: ReadableStream<Uint8Array>,
  options: Options
) {
  const passThrough = new Stream.PassThrough();
  stream.pipeline(
    imageBlob as unknown as NodeJS.ReadableStream,
    passThrough,
    (err) => {
      if (err) {
        logger.log('Error in pipe' + err);
        handleFallback(res, options);
        return;
      }
    }
  );
  passThrough.pipe(res);
}

function handleFallback(res: NextApiResponse, options: Options) {
  if (options.fallbackUrl.trim()) {
    res.redirect(options.fallbackUrl);
  } else {
    res.status(422).send({ message: options.messages.imageFetchError });
  }
}

async function fetchImageBlob(url: string) {
  return await fetch(url, {
    headers: {
      'user-agent':
        'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36',
      accept: 'image/*'
    }
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(
          `Unable to fetch image, received status code ${response.status}`
        );
      }
      return response.body;
    })
    .catch((error) => {
      logger.error(`An error occurred while fetching the image: ${error}`);
    });
}
