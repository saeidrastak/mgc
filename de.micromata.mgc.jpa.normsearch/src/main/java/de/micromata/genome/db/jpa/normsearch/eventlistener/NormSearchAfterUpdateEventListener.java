package de.micromata.genome.db.jpa.normsearch.eventlistener;

import de.micromata.genome.db.jpa.normsearch.NormalizedSearchServiceManager;
import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.Emgr;
import de.micromata.genome.jpa.events.EmgrAfterUpdatedEvent;
import de.micromata.genome.jpa.events.EmgrEventHandler;

/**
 * Updates history after Update.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class NormSearchAfterUpdateEventListener implements EmgrEventHandler<EmgrAfterUpdatedEvent>
{

  @Override
  public void onEvent(EmgrAfterUpdatedEvent event)
  {
    if ((event.getEmgr() instanceof Emgr) == false) {
      return;
    }
    if ((event.getEntity() instanceof DbRecord) == false) {
      return;
    }
    NormalizedSearchServiceManager.get().getNormalizedSearchDAO().update((Emgr) event.getEmgr(),
        (DbRecord) event.getEntity());

  }

}
