package ch.zhaw.parkship.util;

import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Rollback
@Transactional
public abstract class AbstractDataRollbackTest extends AbstractDataTest{
}
