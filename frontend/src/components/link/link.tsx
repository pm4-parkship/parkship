import NextLink from 'next/link';
import { Link as MuiLink } from '@mui/material';
import { useRouter } from 'next/router';
import React, { FC } from 'react';
import makeStyles from '@mui/styles/makeStyles';

export interface LinkProps {
  href?: string;
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
  if (hrefLocal.indexOf('http') === 0) {
    skipLocaleHandling = true;
  }

  if (skipLocaleHandling) {
    return (
      <a href={hrefLocal} target="_blank" {...props}>
        {children}
      </a>
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
      <MuiLink {...props} className={classes.nativeLink} href={hrefLocal}>
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
