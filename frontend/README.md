![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)
![Next JS](https://img.shields.io/badge/Next-black?style=for-the-badge&logo=next.js&logoColor=white)
![NodeJS](https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/githubactions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

# PM4 Project

Development for all PM4 Branded Apps

- [Getting Started](#getting-started)
- [Frontend Libraries](#frontend-libraries)
- [Console.Log Alternative](#console-log-alternative)
- [Recommend Working Tools](#recommend-working-tools)
- [Updating packages](#updating-packages)
- [Using Icons](#using-icons)
- [Using Forms](#using-forms)
- [Using Images](#using-images)
- [Using Links](#using-links)
- [Running unit tests](#unit-tests)
- [Creating Type Interfaces](#create-type-interface)
- [Branching Name Convention](#branching-name-convention)
- [Commit Message Guidelines](#commit-msg-guidelines)
- [Spacing and Indentation](#spacing-and-indentation)

<a name="getting-started"></a>

## Getting started

### Prerequisites

Make sure you have installed all of the following prerequisites on your development machine:

- [VsCode](https://code.visualstudio.com/download) (Can also use any other IDE, but you gotta know how to work with it :))
- [Git](https://git-scm.com/)
- [Node.js with NVM](https://github.com/nvm-sh/nvm]) (Check in the .nvmrc file for the correct version)

### Install dependencies

Run `npm install` to install all dependencies.

### Run the app

Run `npm run dev` to start the development server.

### General Advice

When working on our repository make sure to memorizes this README.md, since it contains all the information you need to know about the project.

#### 1. Branching

**_!!! IMPORTANT !!!_**

**_NO CODE SHOULD BE PUSHED DIRECTLY TO THE MAIN BRANCH, ALWAYS CREATE A NEW BRANCH AND MAKE A PULL REQUEST._**

When working on a new feature/issue/story, make sure to create a new branch, and name it as follows:

`<type>/<issue-number>/<description>`

Example: `feat/PM4-123/add-primary-button`

More informations to that can be found [here](#branching-name-convention)

#### 2. Commit Messages

When committing your changes, make sure to use the following commit message guidelines:

`<type>: [<issue>] <subject>`

Example: `feat: [PM4-123] add primary button`

More informations to that can be found [here](#commit-msg-guidelines)

<a name="frontend-libraries"></a>

## Frontend Libraries

Make sure to use the following libraries for the frontend, to keep the code consistent. Get familiar with the documentation of the libraries, to make sure you're using them correctly.

- [React](https://beta.reactjs.org/learn)
- [Next.js](https://nextjs.org/)
- [TypeScript](https://www.typescriptlang.org/)
- [Material UI](https://mui.com/material-ui/)
- [React Hook Form](https://github.com/dohomi/react-hook-form-mui)
- [ZOD (Schema Validation)](https://zod.dev/)
- [Redux](https://react-redux.js.org/)

<a name="console-log-alternative"></a>

## Console.Log Alternative

As you may already recognized, console.log is banned in our project, since it's not a good practice to use it in production code.

Instead of using console.log, we recommend to use the following commands:

`logger.log('info message')` (Hidden in production)

`logger.warn('warn message')` (Hidden in production

`logger.error('error message')` (Visible in production)

<a name="recommend-working-tools"></a>

## Recommend Working Tools

For software Development we recommend the following tools, totally up to you if you want to use them or not.

The total costs are together around 15$ per month (without GPT premium), which is not that much, and it helps you a lot to improve your Code Quality.

- [Webstorm](https://www.jetbrains.com/de-de/webstorm/)
- [Github Copilot](https://copilot.github.com/)
- [Chat GPT](https://chat.openai.com/chat)

<a name="updating-packages"></a>

## Updating packages

When updating packages, make sure to run the following command:

`npm outdated` and `npm audit`

Then go to the package.json and update the version number, try not to do all at once, do it one by one.

Create a own Branch for these updates, since it can cause to unexpected errors.

<a name="using-icons"></a>

## Using Icons

To find the right Icon, may use this website to browse through all the icons: https://icon-sets.iconify.design/

As soon as you find your desired Icon, you'll find the name of the Icon, which you can use in your code.

```
import { Icon } from '@iconify/react';

<Icon icon="carbon:arrow--up" />
```

<a name="using-forms"></a>

## Using Forms

To use forms, make sure to use the following libraries:

- [React Hook Form](https://github.com/dohomi/react-hook-form-mui)
- [ZOD (Schema Validation)](https://zod.dev/)

### 1. Create a ZOD Schema

First of all, you need to create a type schema, which contains all the fields of your form.

```
  const formSchema = z.object({
    test0: z.string().min(2).max(20),
    test1: z.custom((data) => {
      if (typeof data === 'string') {
        return parseInt(data) <= 18;
      }
      return true;
    }),
  });
```

### 2. Create a custom Error Map (optional, only needed in special cases)

```
  const customErrorMap = () => {
    return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
      if (issue.code === z.ZodIssueCode.custom) {
        if (issue.path.includes('test1')) {
          return {
            message: `It has to be between 1 and 18`
          };
        }
      }
      return { message: ctx.defaultError };
    };
  }; 
```

### 3. Create a Form

```
  const { handleSubmit, control } = useForm<StandardTokenProps>({
    resolver: zodResolver(formSchema, {
      errorMap: customErrorMap()
    }),
    defaultValues: {
      test0: '',
      test1: '',
    }
  });
```

```
      <form
        style={{ display: 'grid', width: '100%', gap: '10px' }}
        onSubmit={handleSubmit((data) => handleSubmitCreate(data))}
      >
        <TextFieldElement
          placeholder="e.g Test"
          control={control}
          name="test0"
          label="Test 0"
          variant="outlined"
          fullWidth
          required
        />
        <TextFieldElement
          placeholder="e.g Test"
          control={control}
          name="test1"
          label="Test 1"
          variant="outlined"
          fullWidth
          required
        />
        <Button type={'submit'} variant={'contained'} color={'primary'}>
          Submit
        </Button>
      </form>
```

<a name="using-images"></a>

## Using Images
To use Images in our Repository make sure to use the following component:

You can find it unter `src/components/image/image-custom.tsx` for more details

```
 <ImageCustom
    localImage
    src={myImageURL}
    alt={'ChainLogo'}
    width={18}
    height={18}
 />
```

<a name="using-links"></a>

## Using Links
To use Links in our Repository make sure to use the following component:

You can find it under `src/components/link/link-custom.tsx` for more details

```
 <LinkCustom
    href={'https://google.com'}
    skipLocaleHandling
  >
    <Typography variant={'body2'}>Google</Typography>
  </LinkCustom>
```


<a name="unit-tests"></a>

## Running unit tests (not yet applied)

Run `npm run test` to execute the unit tests via [Jest](https://jestjs.io).

<a name="create-type-interface"></a>

## Creating Type Interfaces

To create Types as interfaces make sure to store them into a .model.ts file.
This will make sure that the types are getting generated into the types folder.

`/src/models/my-model.model.ts`

<a name="branching-name-convention"></a>

## Branching Name Convention

Branches should be named as follows:

`<type>/<issue-number>/<description>`

Example: `feat/PM4-123/add-primary-button`

### 1. Type

Available types are:

- feat → Changes about addition or removal of a feature. Ex: `feat: add table on landing page`, `feat: remove table from landing page`
- fix → Bug fixing, followed by the bug. Ex: `fix: illustration overflows in mobile view`
- docs → Update documentation (README.md)
- style → Updating style, and not changing any logic in the code (reorder imports, fix whitespace, remove comments)
- chore → Installing new dependencies, or bumping deps
- refactor → Changes in code, same output, but different approach
- ci → Update github workflows, husky
- test → Update testing suite, cypress files
- revert → when reverting commits
- perf → Fixing something regarding performance (deriving state, using memo, callback)

<a name="commit-msg-guidelines"></a>

## Commit Message Guidelines

This repository follows [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).
### Format

`<type>(optional scope): <description>`
Example: `feat: [PM4-123] add primary button`

### 1. Type

Available types are:

- feat → Changes about addition or removal of a feature. Ex: `feat: add table on landing page`, `feat: remove table from landing page`
- fix → Bug fixing, followed by the bug. Ex: `fix: illustration overflows in mobile view`
- docs → Update documentation (README.md)
- style → Updating style, and not changing any logic in the code (reorder imports, fix whitespace, remove comments)
- chore → Installing new dependencies, or bumping deps
- refactor → Changes in code, same output, but different approach
- ci → Update github workflows, husky
- test → Update testing suite, cypress files
- revert → when reverting commits
- perf → Fixing something regarding performance (deriving state, using memo, callback)

### 3. Description

Description must fully explain what is being done.

<a name="spacing-and-indentation"></a>

## Spacing and Indentation

**Please don't use Pixels for spacing and indentation. Use rem instead.**

(There might be some exceptions, but try to avoid it)

| Pixel | REM      |
| ----- | -------- |
| 4 px  | 0.25 rem |
| 8 px  | 0.5 rem  |
| 12 px | 0.75 rem |
| 16 px | 1 rem    |
| 20 px | 1.25 rem |
| 14 px | 1.5 rem  |
| 28 px | 1.75 rem |
| 32 px | 2 rem    |
| 40 px | 2.5 rem  |
