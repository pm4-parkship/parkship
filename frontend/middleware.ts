import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';
import { getIronSession } from 'iron-session/edge';
import { sessionOptions } from './src/auth/session';
import { UserRole } from './src/models';

export const middleware = async (req: NextRequest) => {
  const res = NextResponse.next();
  const session = await getIronSession(req, res, sessionOptions);
  // do anything with session here:
  const { user } = session;

  // Kick out user if not admin
  if (req.url.includes('admin') && user?.role != UserRole.admin) {
    return new NextResponse(null, { status: 403 }); // unauthorized to see pages inside admin/
  }

  return res;
};

export const config = {
  matcher: ''
};
