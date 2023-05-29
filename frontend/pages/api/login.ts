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
      logger.log(data);
      req.session.user = {
        isLoggedIn: true,
        role: data.role,
        token: data.token,
        username: data.username,
        name: data.name,
        greeting: getRandomGreeting(data.name)
      };

      await req.session.save();
      res.send({ user: req.session.user });
    }
  });
}, sessionOptions);
const getRandomGreeting = (name: string): string => {
  const greetings: string[] = [
    `Hallo ${name}!`, // Hello [name]!
    `Guten Tag ${name}!`, // Good day [name]!
    `Schönen Tag noch, ${name}!`, // Have a nice day, [name]!
    `Viel Glück, ${name}!`, // Good luck, [name]!
    `Alles Gute, ${name}!`, // All the best, [name]!
    `Herzlich willkommen, ${name}!`, // Welcome, [name]!
    `Schön Sie zu sehen, ${name}!`, // Nice to see you, [name]!
    `Hoffe es geht Ihnen gut, ${name}!`, // Hope you're doing well, [name]!
    `Geniessen Sie den Tag, ${name}!`, // Enjoy the day, [name]!
    `Beste Grüsse, ${name}!`, // Best regards, [name]!
    `Einen wunderschönen Tag, ${name}!`, // Have a wonderful day, [name]!
    `Freut mich Sie zu treffen, ${name}!` // Nice to meet you, [name]!
  ];

  const randomIndex: number = Math.floor(Math.random() * greetings.length);
  return greetings[randomIndex];
};
