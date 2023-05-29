import { withIronSessionApiRoute } from 'iron-session/next';
import { sessionOptions } from '../../src/auth/sessionconfig';
import { logger } from '../../src/logger';

export default withIronSessionApiRoute(async function loginRoute(req, res) {
  await fetch('http://localhost:8080/backend/auth/signin', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req.body)
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      //FETCH GET REQUEST backend/users/user
      const userResponse = await fetch('http://localhost:8080/backend/users/user', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${data.token}` // Verwenden Sie den Token aus der vorherigen Antwort
        }
      });

      if (userResponse.ok) {
        const userData = await userResponse.json();
        // Hier können Sie die Benutzerdaten verwenden, z.B. userData.username
        data.surname = userData.name;
      }

      // get user from database then:
      logger.log(data);
      req.session.user = {
        isLoggedIn: true,
        role: data.role,
        token: data.token,
        username: data.username,
        surname: data.surname
      };
      await req.session.save();
      res.send({ user: req.session.user });
    }
  });
}, sessionOptions);
