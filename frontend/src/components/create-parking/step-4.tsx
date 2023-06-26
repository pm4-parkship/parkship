import { Grid, Typography } from '@mui/material';
import { TextFieldElement } from 'react-hook-form-mui';
import TagBar from '../search-bar/tag-bar';
import { useEffect, useState } from 'react';
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { StepProps } from './create-parking-lot-stepper';
import { logger } from '../../logger';
import { DUMMY_TAGS, TagData } from '../../models';

const Step4 = ({ navigation, parkingLot, handleNext }: StepProps) => {
  const [selectedTags, setSelectedTag] = useState<TagData[]>([]);

  useEffect(() => {
    setSelectedTag(parkingLot.tags);
  }, [parkingLot]);
  const addTag = (tag: TagData) => {
    setSelectedTag([...selectedTags, tag]);
    logger.log(selectedTags);
  };

  const handleDelete = (key: number) => {
    setSelectedTag((tags) => tags.filter((tag) => tag.id !== key));
  };

  const formSchema = z.object({
    description: z.string().optional(),
    tags: z.any().optional()
  });
  const { handleSubmit, control } = useForm({
    resolver: zodResolver(formSchema),
    mode: 'onSubmit',
    defaultValues: {
      description: parkingLot.description
    }
  });

  return (
    <form
      onSubmit={handleSubmit((data) => {
        parkingLot.description = data.description;
        parkingLot.tags = selectedTags;
        handleNext(parkingLot);
      })}
    >
      <Grid container justifyContent="left" alignItems="center" spacing={3}>
        <Grid item xs={4}>
          <Typography variant="h6">Beschreibung:</Typography>
        </Grid>
        <Grid item xs={12}>
          <TextFieldElement
            fullWidth
            multiline
            rows={4}
            id="description"
            label="Beschreibung: "
            name="description"
            control={control}
          />
        </Grid>
        <Grid item>
          <TagBar
            options={DUMMY_TAGS}
            addTag={addTag}
            handleDelete={handleDelete}
            selected={selectedTags}
          />
        </Grid>
      </Grid>
      {navigation()}
    </form>
  );
};

export default Step4;
