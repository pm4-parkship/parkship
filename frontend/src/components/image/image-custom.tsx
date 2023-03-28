import React from 'react';
import Image, { ImageProps } from 'next/image';

export interface ImageCustomProps extends ImageProps {
  proxyUrl?: string;
  localImage?: boolean;
}

/**
 * ImageCustom
 * @param proxyUrl - proxy url
 * @param src - image source
 * @param width - width standard is 100px
 * @param height - height standard is 100px
 * @param localImage - if true, it will not use the proxyUrl
 * @param className - className
 * @param style - style
 * @param props - other props
 * @constructor
 */
export default function ImageCustom({
  proxyUrl = 'image-logo-proxy',
  src,
  width = 100,
  height = 100,
  localImage = false,
  className,
  style,
  ...props
}: ImageCustomProps) {
  return (
    <Image
      {...props}
      style={style}
      width={width}
      className={className}
      height={height}
      blurDataURL="data:image/jpeg;base64,/9j/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCAAIAAoDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAb/xAAhEAACAQMDBQAAAAAAAAAAAAABAgMABAUGIWEREiMxUf/EABUBAQEAAAAAAAAAAAAAAAAAAAMF/8QAGhEAAgIDAAAAAAAAAAAAAAAAAAECEgMRkf/aAAwDAQACEQMRAD8AltJagyeH0AthI5xdrLcNM91BF5pX2HaH9bcfaSXWGaRmknyJckliyjqTzSlT54b6bk+h0R//2Q=="
      placeholder="blur"
      quality={100}
      onError={(e) => {
        e.currentTarget.srcset = '/placeholder-logo.png';
      }}
      src={localImage ? src : `/api/${proxyUrl}?url=${src}`}
      alt="image"
    />
  );
}
