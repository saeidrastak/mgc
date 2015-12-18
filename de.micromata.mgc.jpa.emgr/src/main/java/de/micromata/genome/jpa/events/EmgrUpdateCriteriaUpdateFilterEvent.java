package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.CriteriaUpdate;
import de.micromata.genome.jpa.IEmgr;

/**
 * Wrapps a de.micromata.genome.jpa.Emgr.update(CriteriaUpdate<T>).
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <E> the element type
 */
public class EmgrUpdateCriteriaUpdateFilterEvent<E>extends EmgrBaseUpdateFilterEvent<E>
{

  /**
   * Instantiates a new emgr update criteria update filter event.
   *
   * @param emgr the emgr
   * @param update the update
   */
  public EmgrUpdateCriteriaUpdateFilterEvent(IEmgr<?> emgr, CriteriaUpdate<E> update)
  {
    super(emgr, update);
  }

}
