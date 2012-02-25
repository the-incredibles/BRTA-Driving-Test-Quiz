package net.incredibles.brtaquiz.dao;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import net.incredibles.brtaquiz.domain.Sign;
import net.incredibles.brtaquiz.domain.SignSet;
import net.incredibles.brtaquiz.service.DbHelperManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sharafat
 * @Created 2/21/12 1:31 AM
 */
@Singleton
public class SignDaoImpl implements SignDao {
    private static final Logger LOG = LoggerFactory.getLogger(SignDaoImpl.class);

    private Dao<Sign, Integer> dao;

    @SuppressWarnings("unchecked")
    public SignDaoImpl() throws SQLException {
        dao = DbHelperManager.getHelper().getDao(Sign.class);
    }

    @Override
    protected void finalize() throws Throwable {
        DbHelperManager.release();
        super.finalize();
    }

    @Override
    public void save(Sign sign) throws SQLException {
        dao.createIfNotExists(sign);
    }

    @Override
    public List<Sign> getRandomSigns(Sign excludedSign, SignSet signSet, long limit) {
        try {
            return dao.query(
                    dao.queryBuilder().orderByRaw("RANDOM()").limit(limit)
                            .where().eq("sign_set_id", signSet.getId()).and().ne("id", excludedSign.getId())
                            .prepare()
            );
        } catch (SQLException e) {
            LOG.error("Query exception", e);
            return null;
        }
    }

    @Override
    public List<Sign> getRandomSignsExcluded(SignSet excludedSignSet, long limit) {
        try {
            return dao.query(
                    dao.queryBuilder().orderByRaw("RANDOM()").limit(limit)
                            .where().ne("sign_set_id", excludedSignSet.getId())
                            .prepare()
            );
        } catch (SQLException e) {
            LOG.error("Query exception", e);
            return null;
        }
    }

    @Override
    public List<Sign> getRandomSigns(SignSet signSet, long limit) {
        try {
            return dao.query(
                    dao.queryBuilder().orderByRaw("RANDOM()").limit(limit)
                            .where().eq("sign_set_id", signSet.getId())
                            .prepare()
            );
        } catch (SQLException e) {
            LOG.error("Query exception", e);
            return null;
        }
    }

    @Override
    public List<Sign> getRandomSignsForSignRecognitionSet(long limit) {
        assert limit % 5 == 0;

        long maxRows = limit / 5;
        List<Sign> signList = new ArrayList<Sign>();
        try {
            signList.add(getSign(maxRows, 1));
            signList.add(getSign(maxRows, 2));
            signList.add(getSign(maxRows, 3));
            signList.add(getSign(maxRows, 4));
            signList.add(getSign(maxRows, 5));

            return signList;
        } catch (SQLException e) {
            LOG.error("Query exception", e);
            return null;
        }
    }

    private Sign getSign(long maxRows, int signSetId) throws SQLException {
        return dao.query(dao.queryBuilder().orderByRaw("RANDOM()").limit(maxRows).where().eq("sign_set_id", signSetId)
                .prepare()).get(0);
    }
}
