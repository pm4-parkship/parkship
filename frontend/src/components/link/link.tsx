import NextLink from 'next/link';
import { Link as MuiLink } from '@mui/material';
import { useRouter } from 'next/router';
import React, { FC } from 'react';
import makeStyles from '@mui/styles/makeStyles';
import { UrlObject } from 'url';

declare type Url = string | UrlObject;

export interface LinkProps {
  href?: Url;
  children: React.ReactNode;
  type?: 'link' | 'button';
  skipLocaleHandling?: boolean;
}

const Link: FC<LinkProps> = ({
  href,
  children,
  skipLocaleHandling = false,
  ...props
}) => {
  const classes = useStyles();
  const router = useRouter();

  const hrefLocal = href || router.asPath;

  if (skipLocaleHandling && typeof hrefLocal === 'string') {
    return (
      <a href={hrefLocal} target="_blank" {...props}>
        {children}
      </a>
    );
  }

  if (!skipLocaleHandling && typeof href === 'object') {
    return (
      <NextLink {...props} href={href} className={classes.nativeLink}>
        <MuiLink {...props} className={classes.nativeLink}>
          {children}
        </MuiLink>
      </NextLink>
    );
  }

  return (
    <NextLink
      {...props}
      href={hrefLocal}
      passHref
      legacyBehavior
      className={classes.nativeLink}
    >
      <MuiLink {...props} className={classes.nativeLink}>
        {children}
      </MuiLink>
    </NextLink>
  );
};

export default Link;

const useStyles = makeStyles(() => ({
  nativeLink: {
    textDecoration: 'none',
    color: 'inherit'
  }
}));
