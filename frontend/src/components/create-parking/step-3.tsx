import { Button, Grid } from '@mui/material';
import { toast } from 'react-toastify';
import { Offer } from './offer';
import { useEffect, useState } from 'react';
import { OfferModel } from '../../models';
import AddIcon from '@mui/icons-material/Add';
import { logger } from '../../logger';
import { StepProps } from './create-parking-lot-stepper';
import { addDays, startOfToday } from 'date-fns';

const MAX_OFFER_COUNT = 5;
const Step3 = ({ navigation, parkingLot, handleNext }: StepProps) => {
  const [offers, setOffers] = useState<OfferModel[]>([]);
  const [futureOffersCount, setFutureOfferCount] = useState<Set<number>>(
    new Set()
  );

  useEffect(() => {
    setOffers(parkingLot.offers);
    logger.log(offers);
  }, [parkingLot]);

  const updateOffer = (key: number, updatedOffer: OfferModel) => {
    logger.log(updatedOffer);
    const newOffers = [...offers];
    if (key >= 0 && key < newOffers.length) {
      newOffers[key] = updatedOffer;
      setOffers(() => newOffers);

      if (updatedOffer.from >= startOfToday()) {
        setFutureOfferCount(
          () => new Set(futureOffersCount.add(updatedOffer.id!))
        );
      } else {
        futureOffersCount.delete(updatedOffer.id!);
        setFutureOfferCount(() => new Set(futureOffersCount));
      }
    }
  };
  const deleteOffer = (key: number, offer: OfferModel) => {
    const newOffers = offers.filter((value, index) => index !== key);
    setOffers(newOffers);

    futureOffersCount.delete(offer.id!);
    setFutureOfferCount(() => new Set(futureOffersCount));
  };
  const addNewOffer = () => {
    const startDate =
      offers.length === 0
        ? new Date()
        : new Date(new Date(offers[offers.length - 1].to));

    logger.log(startDate);
    const newOffer: OfferModel = {
      id: new Date().getMilliseconds(),
      from: startDate,
      to: addDays(startDate, 7),
      monday: false,
      tuesday: false,
      wednesday: false,
      thursday: false,
      friday: false,
      saturday: false,
      sunday: false
    };
    setOffers(() => [...offers, newOffer]);
    setFutureOfferCount(() => new Set(futureOffersCount.add(newOffer.id!)));
  };

  return (
    <form
      onSubmit={(e) => {
        parkingLot.offers = offers;
        e.preventDefault();
        handleNext(parkingLot);
      }}
    >
      <Grid container justifyContent="left" alignItems="center">
        <Grid
          container
          justifyContent="left"
          alignItems="center"
          spacing={3}
          style={{ textAlign: 'center' }}
        >
          <Grid item xs={12}>
            <Button
              variant="contained"
              type="button"
              onClick={() => {
                if (futureOffersCount.size < MAX_OFFER_COUNT) {
                  addNewOffer();
                } else {
                  toast.error('Maximal 5 Angebote möglich');
                }
              }}
              disabled={futureOffersCount.size >= MAX_OFFER_COUNT}
              startIcon={<AddIcon />}
            >
              Buchbaren Zeitraum hinzufügen
            </Button>
          </Grid>
        </Grid>
        {offers.map((offer, index) => {
          return (
            <Offer
              key={offer.id}
              onValuesChange={(data) => updateOffer(index, data)}
              deleteOffer={() => deleteOffer(index, offer)}
              offerData={offer}
              disable={new Date(offer.from) < startOfToday()}
            />
          );
        })}
      </Grid>

      {navigation()}
    </form>
  );
};

export default Step3;
