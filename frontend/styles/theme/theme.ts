const palette = {
  light: {
    primary: {
      main: '#8bccd6',
      light: '#A2D6DE',
    },
    secondary: {
      main: '#AED9E0',
    },
    error: {
      main: '#FF7C70',
    },
    warning: {
      main: '#f0da94',
    },
    success: {
      main: '#70a288',
    },
    text: {
      primary: '#494E5A',
      secondary: 'rgba(73,78,90,0.5)',
      disabled: 'rgba(73,78,90,0.38)',
      hint: 'rgba(73,78,90,0.38)',
    },
    divider: 'rgba(73,78,90,0.12)',
    background: {
      paper: '#e0f0ff',
      default: '#ffffff'
    },
  },
  dark: {
    primary: {
      main: '#3C62DF',
      light: '#061121'
    },
    background: {
      paper: '#1a1e27',
      default: '#1a1e27'
    },
    secondary: {
      main: '#1a1e27',
      light: '#061121'
    },
    divider: '#ADB2B7',
    text: {
      primary: '#9aa4b3',
      secondary: '#CDCFFB'
    }
  }
};
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const getDesignTokens = (mode: any) => ({
  palette: {
    mode,
    ...(mode === 'light'
      ? {
          primary: {
            main: palette.light.primary.main,
            light: palette.light.primary.light
          },
          divider: palette.light.divider,
          background: {
            default: palette.light.background.default,
            paper: palette.light.background.paper
          },
          text: {
            primary: palette.light.text.primary,
            secondary: palette.light.text.secondary
          }
        }
      : {
          primary: {
            main: palette.dark.primary.main,
            light: palette.dark.primary.light
          },
          background: {
            default: palette.dark.background.default,
            paper: palette.dark.background.paper
          },
          divider: palette.dark.divider,
          text: {
            primary: palette.dark.text.primary,
            secondary: palette.dark.text.secondary
          }
        })
  },
  typography: {
    color:
      mode === 'light' ? palette.light.text.primary : palette.dark.text.primary,
    fontFamily: [
      'Oswald',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif'
    ].join(','),
    h1: {
      fontSize: '2rem',
      fontWeight: 600
    },
    h2: {
      fontSize: '1.75rem',
      fontWeight: 400
    },
    h3: {
      fontWeight: 800,
      fontSize: '1.5rem'
    },
    h6: {
      fontSize: '1.25rem'
    },
    body1: {
      fontSize: '1rem',
      fontWeight: 700
    },
    body2: {
      fontSize: '0.75rem'
    },
    subtitle2: {
      fontWeight: 1000,
      fontSize: '0.8rem',
      letterSpacing: '0.063rem'
    }
  },
  breakpoints: {
    values: {
      xs: 0,
      sm: 600,
      md: 1024,
      lg: 1200,
      xl: 1536
    }
  },
  components: {
    MuiDivider: {
      styleOverrides: {
        root: {
          margin: '20px'
        }
      }
    },
    MuiCalendarOrClockPicker: {
      styleOverrides: {
        root: {
          backgroundColor:
            mode === 'light'
              ? palette.light.background.default
              : palette.dark.background.default
        }
      }
    },
    MuiPickersDay: {
      styleOverrides: {
        root: {
          backgroundColor:
            mode === 'light'
              ? palette.light.background.default
              : palette.dark.background.default
        }
      }
    },
    MuiModal: {
      styleOverrides: {
        root: {}
      }
    },
    MuiTableRow: {
      styleOverrides: {
        root: {
          border: 0
        }
      }
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          border: 0,
          padding: '0 0 0 0'
        }
      }
    },
    MuiPickersToolbarButton: {
      styleOverrides: {
        root: {
          margin: '0'
        }
      }
    },
    MuiStepContent: {
      styleOverrides: {
        root: {
          paddingLeft: '4rem',
          marginLeft: '0.85rem',
          marginBottom: '1.1rem'
        }
      }
    },
    MuiStepConnector: {
      styleOverrides: {
        root: {
          border: 0,
          marginLeft: '20px',
          display: 'none'
        }
      }
    },
    // Not sure why this doesn't work as styled component
    MuiStepLabel: {
      styleOverrides: {
        iconContainer: {
          paddingRight: '1.8rem',
          '& .MuiStepIcon-root': {
            width: '1.8rem',
            height: '1.8rem'
          },
          color:
            mode === 'light'
              ? palette.light.text.primary
              : palette.dark.text.primary
        },
        label: {
          paddingLeft: '20px',
          fontWeight: 500,
          fontSize: '1.3125rem',
          '&.Mui-completed': {
            '.completed-step-suffix': {
              display: 'inline-block'
            }
          },
          '.completed-step-suffix': {
            display: 'none'
          },
          color:
            mode === 'light'
              ? palette.light.text.primary
              : palette.dark.text.primary
        }
      }
    },
    MuiTab: {
      styleOverrides: {
        root: {
          '&.Mui-selected': {
            boxShadow: 'rgba(0, 0, 0, 0.56) 0px 22px 70px 4px',
            color:
              mode === 'light'
                ? palette.light.text.primary
                : palette.dark.text.primary
          },
          color:
            mode === 'light'
              ? palette.light.text.primary
              : palette.dark.text.primary
        }
      }
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          padding: '10px',
          borderColor: mode === 'dark' ? '#313d50' : 'lightgrey',
          borderWidth: mode === 'dark' ? '2px ' : '1px ',
          backgroundColor: mode === 'dark' ? '#0d131d' : '#ffffff',
          margin: '0 auto'
        }
      },
      defaultProps: {
        elevation: 2
      }
    },
    MuiChip: {
      styleOverrides: {
        root: {
          color: mode === 'dark' ? '#FFFFFF' : '#000000',
          backgroundColor: mode === 'dark' ? '#0d131d' : '#FFFFFF'
        }
      }
    },
    MuiButton: {
      styleOverrides: {
        root: {
          margin: '10px'
        }
      }
    },
    zIndex: {
      appBar: 1200,
      drawer: 1100
    }
  }
});
