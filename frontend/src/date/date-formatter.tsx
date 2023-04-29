import { format } from 'date-fns';
import { de } from 'date-fns/locale';

export const formatDate = (date: Date): string => {
  return format(date, 'P', { locale: de });
};
