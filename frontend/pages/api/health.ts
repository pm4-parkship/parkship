import { Request, Response } from 'express';

export default function health(req: Request, res: Response) {
  res.status(200).json({ status: 'ok' });
}
