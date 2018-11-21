import re
import geoip2.database

log = '2018-10-02 00:00:40,804 INFO  [qtp509886383-2438084:https://192.168.15.15:7071/service/admin/soap/] [name=cjordesonm8_20@ucab.edu.ve;ip=192.168.15.15;] security - cmd=Auth; account=cjordesonm8_20@ucab.edu.ve; protocol=soap;'

geo = geoip2.database.Reader ('GeoLite2-City.mmdb')

def process_log (p_log):
    data = {}

    ips = re.search ('oip=(\d+.\d+.\d+.\d+);', p_log)
    emails = re.search ('account=(\w+@[a-zA-Z\.]+);', p_log)
    times = re.search ('^\d+\-\d+\-\d+\s(\d+\:\d+\:\d+),', p_log)
    if not ips:
        data['country'] = None
        data['city'] = None
        data['ip'] = None
    else:
        print (ips.group (1))
        r = geo.city (ips.group (1))
        data['country'] = r.country.name
        data['city'] = r.city.name
        data['ip'] = ips.group (1)

    # print (emails.groups ())
    if not emails:
        data['email'] = None
    else:
        data['country'] = emails.group (1)

    time = times.group (1)
    hour = time.split (':')[0]

    data['hour'] = hour
    print (data)

    return data

process_log (log)