import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';
import { getIronSession } from 'iron-session/edge';
import { UserRole } from 'src/models';
import { sessionOptions } from 'src/auth/sessionconfig';

export const middleware = async (req: NextRequest) => {
  const res = NextResponse.next();
  const session = await getIronSession(req, res, sessionOptions);
  const { user } = session;

  if (req.url.includes('admin') && user?.role === UserRole.ADMIN) {
    return NextResponse.redirect(new URL('/login', req.url));
  }

  return res;
};

export const config = {
  matcher: ''
};
