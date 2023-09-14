///////////////////////////////////////////////////////////////////////////////////////////////
// Octagon.
// Copyright (C) 2023-2023 the original author or authors.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2
// of the License only.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
///////////////////////////////////////////////////////////////////////////////////////////////
package org.nanoboot.octagon.persistence.impl.mock;

import java.util.ArrayList;
import java.util.List;
import org.nanoboot.octagon.entity.Website;
import org.nanoboot.octagon.persistence.api.WebsiteRepo;

/**
 *
 * @author robertvokac
 */
public class WebsiteRepoImplMock implements WebsiteRepo {

    private final List<Website> internalList = new ArrayList<>();

    private int nextNumber = 1;

    @Override
    public List<Website> list(
            int pageNumber,
            int pageSize,
            Boolean contentVerified,
            String archiveVerified,
            String recording,
            Integer number,
            String url,
            Integer variantNumber) {
        if (internalList.isEmpty()) {
            for (int i = 0; i < 50; i++) {
                internalList.add(
                        new Website(
                                nextNumber++,
                                "http://colorshapes.nanoboot.org",
                                null,
                                "abc",
                                "en",
                                true,
                                null,
                                null,
                                "",
                                null,
                                null));
            }
        }
        List<Website> finalList = new ArrayList<>();
        int numberEnd = pageSize * pageNumber;
        int numberStart = numberEnd - pageSize + 1;
        for (Website w : internalList) {
            if (w.getNumber() < numberStart || w.getNumber() > numberEnd) {
                continue;
            }

            if (number != null) {
                if (w.getNumber().intValue() == number.intValue()) {
                    finalList.add(w);
                    break;
                } else {
                    continue;
                }
            }
            if (url != null) {
                if (w.getUrl().contains(url)) {
                    finalList.add(w);
                    continue;
                } else {
                    continue;
                }
            }

            finalList.add(w);

        }
        return finalList;
    }

    @Override
    public int create(Website website) {
        website.setNumber(nextNumber++);
        internalList.add(website);
        return website.getNumber();
    }

    @Override
    public Website read(Integer number) {
        for (Website w : internalList) {
            if (w.getNumber().intValue() == number.intValue()) {
                return w;
            }
        }
        return null;
    }

    @Override
    public void update(Website website) {
        Website websiteToBeDeleted = null;
        for (Website w : internalList) {
            if (w.getNumber().intValue() == website.getNumber().intValue()) {
                websiteToBeDeleted = w;
                break;
            }
        }
        if (websiteToBeDeleted == null) {
            //nothing to do
            return;
        }
        internalList.remove(websiteToBeDeleted);
        internalList.add(website);

    }
    public boolean hasSuchUrl(String url) {
        return false;
    }

    @Override
    public long archived() {
        return 0;
    }

    @Override
    public long notArchived() {
        return 0;
    }
}
